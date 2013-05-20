package com.iuwcity.memcachedha;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.iuwcity.memcachedha.client.Client;
import com.iuwcity.memcachedha.client.ClientFactory;
import com.iuwcity.memcachedha.client.Command;
import com.iuwcity.memcachedha.client.CommandPrototype;
import com.iuwcity.memcachedha.listener.CommandListener;
import com.iuwcity.memcachedha.listener.ServerStatListener;
import com.iuwcity.memcachedha.protocol.CommandEnum;
import com.iuwcity.memcachedha.protocol.binary.BinaryCommandFactory;

public class MemcachedClientDispatch {

	private static final Log LOG = LogFactory.getLog(MemcachedClientDispatch.class);

	private Set<String> syncMethods;

	private int primaryRetry = 2;

	private int initalTime = 1000;

	private int breathTime = 1000;

	private int syncThread = 32;

	private int reloadThread = 32;

	private ClientFactory clientFactory = new ClientFactory();

	private List<MemcachedClientKeeper<?>> keepers = new CopyOnWriteArrayList<MemcachedClientKeeper<?>>();

	private List<ScheduledFuture<?>> keeperBreathSchedules = new CopyOnWriteArrayList<ScheduledFuture<?>>();

	private ExecutorService syncService = Executors.newFixedThreadPool(syncThread);

	private ExecutorService reloadService = Executors.newFixedThreadPool(reloadThread);

	private AtomicInteger clientNumbers = new AtomicInteger(0);

	private CommandListener syncCommandListener;

	private CommandListener reloadcCommandListener;

	private ServerStatListener serverStatListener;

	public List<MemcachedClientKeeper<?>> getKeepers() {
		return keepers;
	}

	public void setKeepers(List<MemcachedClientKeeper<?>> keepers) {
		this.keepers = keepers;
	}

	public List<ScheduledFuture<?>> getKeeperBreathSchedules() {
		return keeperBreathSchedules;
	}

	public void setKeeperBreathSchedules(List<ScheduledFuture<?>> keeperBreathSchedules) {
		this.keeperBreathSchedules = keeperBreathSchedules;
	}

	public ExecutorService getSyncService() {
		return syncService;
	}

	public void setSyncService(ExecutorService syncService) {
		this.syncService = syncService;
	}

	public AtomicInteger getClientNumbers() {
		return clientNumbers;
	}

	public void setClientNumbers(AtomicInteger clientNumbers) {
		this.clientNumbers = clientNumbers;
	}

	public CommandListener getSyncCommandListener() {
		return syncCommandListener;
	}

	public void setSyncCommandListener(CommandListener syncCommandListener) {
		this.syncCommandListener = syncCommandListener;
	}

	public CommandListener getReloadcCommandListener() {
		return reloadcCommandListener;
	}

	public void setReloadcCommandListener(CommandListener reloadcCommandListener) {
		this.reloadcCommandListener = reloadcCommandListener;
	}

	public ServerStatListener getServerStatListener() {
		return serverStatListener;
	}

	public void setServerStatListener(ServerStatListener serverStatListener) {
		this.serverStatListener = serverStatListener;
	}

	public static Log getLog() {
		return LOG;
	}

	public Set<String> getSyncMethods() {
		return syncMethods;
	}

	public void setSyncMethods(Set<String> syncMethods) {
		this.syncMethods = syncMethods;
	}

	public int getPrimaryRetry() {
		return primaryRetry;
	}

	public void setPrimaryRetry(int primaryRetry) {
		this.primaryRetry = primaryRetry;
	}

	public int getInitalTime() {
		return initalTime;
	}

	public void setInitalTime(int initalTime) {
		this.initalTime = initalTime;
	}

	public int getBreathTime() {
		return breathTime;
	}

	public void setBreathTime(int breathTime) {
		this.breathTime = breathTime;
	}

	public int getSyncThread() {
		return syncThread;
	}

	public void setSyncThread(int syncThread) {
		this.syncThread = syncThread;
	}

	// public void addSyncMethod(final String methodName){
	// if(this.syncMethods == null ) {
	// this.syncMethods = new TreeSet<String>();
	// }
	// this.syncMethods.add(methodName);
	// }

	public int getReloadThread() {
		return reloadThread;
	}

	public void setReloadThread(int reloadThread) {
		this.reloadThread = reloadThread;
	}

	public synchronized void addKeeper(final MemcachedClientKeeper<?> keeper) throws IOException {

		Client client = clientFactory.getDefaultClient(keeper.getIp(), keeper.getPort());
		keeper.setClient(client);
		keepers.add(keeper);

		ScheduledFuture<?> scheduleFuture = Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new BreathLogic(keeper),
				this.initalTime, this.breathTime, TimeUnit.MILLISECONDS);
		keeperBreathSchedules.add(scheduleFuture);
		clientNumbers.incrementAndGet();
	}

	public Object command(final String key, final CommandParam param) throws SecurityException, IllegalArgumentException,
			NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return command(key, param, true, ReloadLevel.UNRELOAD, 0);
	}

	public Object command(final String key, final CommandParam param, boolean needSync) throws SecurityException,
			IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return command(key, param, needSync, ReloadLevel.UNRELOAD, 0);
	}

	public Object command(final String key, final CommandParam param, final boolean needSync, final int reload,
			final int reloadExpiry) throws SecurityException, IllegalArgumentException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException {
		long hashCode = (long) key.hashCode();
		if (hashCode < 0) {
			hashCode = -hashCode;
		}

		Object response = null;
		int n = (int) hashCode % clientNumbers.get();
		MemcachedClientKeeper<?> primaryKeeper = keepers.get(n);

		try {

			if (primaryKeeper != null && primaryKeeper.getStatus() == Stat.STAT_ONLINE) {
				int retry = 1;
				while ((response = primaryKeeper.invokeMethod(param)) == null && (retry <= this.primaryRetry)) {
					System.out.println("do primary retry " + primaryKeeper.getIp() + ":" + primaryKeeper.getPort());
					retry++;
				}
			}

		} catch (Exception pex) {
			// breath not touch
			System.out.println("breath not touch " + pex.getMessage() + " at " + primaryKeeper.getIp() + ":"
					+ primaryKeeper.getPort());
		}

		List<MemcachedClientKeeper<?>> brokenPeers = new ArrayList<MemcachedClientKeeper<?>>(keepers.size());

		if (response == null) {

			if (reload == ReloadLevel.ONLYMISS) {
				brokenPeers.add(primaryKeeper);
			}

			for (int c = 0; c < keepers.size(); c++) {
				MemcachedClientKeeper<?> tailKeeper = keepers.get(c);

				if (tailKeeper == null || tailKeeper.getStatus() == Stat.STAT_OUTLINE) {
					continue;
				}

				try {
					response = tailKeeper.invokeMethod(param);
				} catch (Exception ex) {
					// breath not touch
					System.out.println("breath not touch " + ex.getMessage() + " at " + tailKeeper.getIp() + ":"
							+ primaryKeeper.getPort());
				}

				if (response != null) {
					primaryKeeper = tailKeeper;
					break;
				} else {
					if (reload == ReloadLevel.ONLYMISS) {
						brokenPeers.add(tailKeeper);
					}
				}
			}
		}

		// sync data
		if (needSync && syncMethods != null && !syncMethods.isEmpty() && syncMethods.contains(param.getMethodName())) {
			syncService.execute(new SyncLogic(primaryKeeper, param));
		}

		// broken reload
		if (response != null) {
			if (reload == ReloadLevel.ONLYMISS && brokenPeers.size() > 0) {
				ReloadParam reloadParam = new ReloadParam(key, reloadExpiry, response);
				reloadService.execute(new ReloadLogic(brokenPeers, primaryKeeper, reloadParam));
			} else if (reload == ReloadLevel.CHECKALL) {
				ReloadParam reloadParam = new ReloadParam(key, reloadExpiry, response);
				reloadService.execute(new ReloadLogic(keepers, primaryKeeper, reloadParam));
			}
		}

		return response;

	}

	private void downKeeper(MemcachedClientKeeper<?> keeper) {
		keeper.setStatus(Stat.STAT_OUTLINE);
		clientNumbers.decrementAndGet();
		if (serverStatListener != null) {
			serverStatListener.serverDown(keeper, clientNumbers.get());
		}
	}

	private void upKeeper(MemcachedClientKeeper<?> keeper) {
		clientNumbers.incrementAndGet();
		keeper.setStatus(Stat.STAT_ONLINE);
		if (serverStatListener != null) {
			serverStatListener.serverUp(keeper, clientNumbers.get());
		}
	}

	public class ReloadLogic implements Runnable {
		List<MemcachedClientKeeper<?>> brokenKeepers;
		MemcachedClientKeeper<?> excludeKeeper;
		ReloadParam param;

		public ReloadLogic(List<MemcachedClientKeeper<?>> brokenKeepers, MemcachedClientKeeper<?> excludeKeeper,
				final ReloadParam param) {
			this.brokenKeepers = brokenKeepers;
			this.excludeKeeper = excludeKeeper;
			this.param = param;
		}

		@Override
		public void run() {
			CommandParam commandParm = null;
			boolean hasListener = syncCommandListener != null;
			if (hasListener) {
				commandParm = new CommandParam(param.getCommandName(),
						new Class[] { String.class, Integer.class, String.class }, new Object[] { param.getKey(),
								param.getExpiry(), param.getValue() });
			}

			Map<MemcachedClientKeeper<?>, Object> responseMap = new HashMap<MemcachedClientKeeper<?>, Object>();

			for (int c = 0; c < brokenKeepers.size(); c++) {
				MemcachedClientKeeper<?> tailKeeper = brokenKeepers.get(c);

				if (excludeKeeper == null || excludeKeeper.equals(tailKeeper) || tailKeeper == null
						|| tailKeeper.getStatus() == Stat.STAT_OUTLINE) {
					continue;
				}

				Object response = null;
				try {
					Client client = tailKeeper.getClient();

					CommandPrototype cp = new CommandPrototype("get", "testDC", 0, null);
					Command command = BinaryCommandFactory.factoryCommand(CommandEnum.Get, cp);
					response = client.sendCommand(command);

					if (hasListener) {
						reloadcCommandListener.onCommandResponsed(tailKeeper, commandParm, response);
					}

				} catch (Exception ex) {
					// breath not touch

					System.out.println("breath not touch " + ex.getMessage() + " at " + tailKeeper.getIp() + ":"
							+ tailKeeper.getPort());

					if (hasListener) {
						reloadcCommandListener.onCommandError(tailKeeper, commandParm);
					}
				}

				if (hasListener) {
					responseMap.put(tailKeeper, response);
				}
			}

			if (hasListener) {
				reloadcCommandListener.onAllCommandResponsed(responseMap, commandParm);
			}
		}
	}

	public class SyncLogic implements Runnable {

		CommandParam param;
		MemcachedClientKeeper<?> primaryKeeper;

		public SyncLogic(MemcachedClientKeeper<?> primaryKeeper, final CommandParam param) {
			this.primaryKeeper = primaryKeeper;
			this.param = param;
		}

		@Override
		public void run() {

			boolean hasListener = syncCommandListener != null;

			Map<MemcachedClientKeeper<?>, Object> responseMap = new HashMap<MemcachedClientKeeper<?>, Object>();

			for (int c = 0; c < keepers.size(); c++) {
				MemcachedClientKeeper<?> tailKeeper = keepers.get(c);

				if (primaryKeeper == null || primaryKeeper.equals(tailKeeper) || tailKeeper == null
						|| tailKeeper.getStatus() == Stat.STAT_OUTLINE) {
					continue;
				}
				Object response = null;
				try {
					response = tailKeeper.invokeMethod(param);

					if (hasListener) {
						syncCommandListener.onCommandResponsed(tailKeeper, param, response);
					}

				} catch (Exception ex) {
					// breath not touch
					if(LOG.isDebugEnabled()){
						LOG.debug("breath not touch " + ex.getMessage() + " at " + tailKeeper.getIp() + ":"
							+ tailKeeper.getPort());
					}

					if (hasListener) {
						syncCommandListener.onCommandError(tailKeeper, param);
					}

				}

				if (hasListener) {
					responseMap.put(tailKeeper, response);
				}
			}

			if (hasListener) {
				syncCommandListener.onAllCommandResponsed(responseMap, param);
			}
		}
	}

	public class BreathLogic implements Runnable {

		private MemcachedClientKeeper<?> keeper;
		private SocketChannel sc;

		public BreathLogic(MemcachedClientKeeper<?> keeper) throws IOException {
			this.keeper = keeper;
			this.sc = SocketChannel.open();
			this.sc.connect(new InetSocketAddress(keeper.getIp(), keeper.getPort()));
		}

		@Override
		public void run() {
			try {

				System.out.println("BreathLogic .. " + keeper.getIp() + ":" + keeper.getPort() + " stats : "
						+ keeper.getStatus());

				if (keeper.getStatus() == Stat.STAT_OUTLINE) {
					this.sc.close();
					this.sc = SocketChannel.open();
					this.sc.connect(new InetSocketAddress(keeper.getIp(), keeper.getPort()));
				}

				if (isServerOnline()) {
					if (keeper.getStatus() == Stat.STAT_OUTLINE) {
						upKeeper(keeper);
					}
				} else if (keeper.getStatus() == Stat.STAT_ONLINE) {
					downKeeper(keeper);
				}

			} catch (Exception e) {
				if (keeper.getStatus() == Stat.STAT_ONLINE) {
					System.out.println("server down " + keeper.getIp() + ":" + keeper.getPort());
					downKeeper(keeper);
				}

			}

		}

		private boolean isServerOnline() {

			try {
				Command command = BinaryCommandFactory.factoryCommand(CommandEnum.Version, null);
				Object response = keeper.getClient().sendCommand(command);
				System.out.println("responseCommand : " + response);
				if (response != null) {
					return true;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return false;
		}

	}
}
