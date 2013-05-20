package com.iuwcity.memcachedha.protocol.binary;

import com.iuwcity.memcachedha.client.BinaryCommand;
import com.iuwcity.memcachedha.client.CommandPrototype;
import com.iuwcity.memcachedha.protocol.CommandEnum;

public class BinaryCommandFactory {

	public static BinaryCommand factoryCommand(CommandEnum commandEnum, CommandPrototype prototype) {
		return buildCommand(commandEnum, prototype);
	}

	private static BinaryCommand buildCommand(CommandEnum commandEnum, CommandPrototype prototype) {
		BinaryCommand command = new BinaryCommand();

		command.setBuffer(new BinaryProtocolParser().parserBuffer(commandEnum, prototype));

		return command;
	}

}
