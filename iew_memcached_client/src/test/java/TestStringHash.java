
public class TestStringHash {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String[] keys = new String[]{"asb","sha_sdf","iuf","123"};
		int moduleSize = 10;
		for(String key : keys){
//			System.out.print(key.hashCode() + " % " + i + " = ");
//			System.out.println(key.hashCode() % i ++ );
			
			long hashCode = (long)key.hashCode();
			
			if (hashCode < 0)
				hashCode = -hashCode;
			
			int moudleNum = (int)hashCode % moduleSize;
			
			System.out.println(moudleNum );
		}
	}

}
