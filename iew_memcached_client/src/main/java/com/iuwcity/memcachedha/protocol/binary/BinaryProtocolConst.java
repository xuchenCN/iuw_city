package com.iuwcity.memcachedha.protocol.binary;

public interface BinaryProtocolConst {
	
	public static final int HEADER_LEN = 24;
	
	public static final byte EMPTY_BYTE = 0x00;
	public static final short EMPTY_SHORT = 0x0000;
	public static final int EMPTY_INT = 0x00000000;
	public static final long EMPTY_LONG = 0x0000000000000000;

	public interface Magic {
		public static final byte REQUEST_MAIGIC = (byte) 0x80;
		public static final byte RESPONSE_MAIGIC = (byte) 0x81;
	}

	public interface Opcodes {
		public static final byte GET = (byte) 0x00;
		public static final byte SET = (byte) 0x01;
		public static final byte ADD = (byte) 0x02;
		public static final byte REPLACE = (byte) 0x03;
		public static final byte DELETE = (byte) 0x04;
		public static final byte INCREMENT = (byte) 0x05;
		public static final byte DECREMENT = (byte) 0x06;
		public static final byte QUIT = (byte) 0x07;
		public static final byte FLUSH = (byte) 0x08;
		public static final byte GETQ = (byte) 0x09;
		public static final byte NO_OP = (byte) 0x0a;
		public static final byte VERSION = (byte) 0x0b;
		public static final byte GETK = (byte) 0x0c;
		public static final byte GETKQ = (byte) 0x0d;
		public static final byte APPEND = (byte) 0x0e;
		public static final byte PREPEND = (byte) 0x0f;
		public static final byte STAT = (byte) 0x10;
		public static final byte SETQ = (byte) 0x11;
		public static final byte ADDQ = (byte) 0x12;
		public static final byte REPLACEQ = (byte) 0x13;
		public static final byte DELETEQ = (byte) 0x14;
		public static final byte INCREMENTQ = (byte) 0x15;
		public static final byte DECREMENTQ = (byte) 0x16;
		public static final byte QUITQ = (byte) 0x17;
		public static final byte FLUSHQ = (byte) 0x18;
		public static final byte APPENDQ = (byte) 0x19;
		public static final byte PREPENDQ = (byte) 0x1a;
		public static final byte VERBOSITY = (byte) 0x1b;
		public static final byte TOUCH = (byte) 0x1c;
		public static final byte GAT = (byte) 0x1d;
		public static final byte GATQ = (byte) 0x1e;
		public static final byte SASL_LIST_MECHS = (byte) 0x20;
		public static final byte SASL_AUTH = (byte) 0x21;
		public static final byte SASL_STEP = (byte) 0x22;
		public static final byte RGET = (byte) 0x30;
		public static final byte RSET = (byte) 0x31;
		public static final byte RSETQ = (byte) 0x32;
		public static final byte RAPPEND = (byte) 0x33;
		public static final byte RAPPENDQ = (byte) 0x34;
		public static final byte RPREPEND = (byte) 0x35;
		public static final byte RPREPENDQ = (byte) 0x36;
		public static final byte RDELETE = (byte) 0x37;
		public static final byte RDELETEQ = (byte) 0x38;
		public static final byte RINCR = (byte) 0x39;
		public static final byte RINCRQ = (byte) 0x3a;
		public static final byte RDECR = (byte) 0x3b;
		public static final byte RDECRQ = (byte) 0x3c;
		public static final byte SET_VBUCKET = (byte) 0x3d;
		public static final byte GET_VBUCKET = (byte) 0x3e;
		public static final byte DEL_VBUCKET = (byte) 0x3f;
		public static final byte TAP_CONNECT = (byte) 0x40;
		public static final byte TAP_MUTATION = (byte) 0x41;
		public static final byte TAP_DELETE = (byte) 0x42;
		public static final byte TAP_FLUSH = (byte) 0x43;
		public static final byte TAP_OPAQUE = (byte) 0x44;
		public static final byte TAP_VBUCKET_SET = (byte) 0x45;
		public static final byte TAP_CHECKPOINT_START = (byte) 0x46;
		public static final byte TAP_CHECKPOINT_END = (byte) 0x47;

	}
	
	public interface ResponseStatus {
	
		public static final short NO_ERROR                               = 0x0000	;
		public static final short KEY_NOT_FOUND                          = 0x0001	;
		public static final short KEY_EXISTS                             = 0x0002	;
		public static final short VALUE_TOO_LARGE                        = 0x0003	;
		public static final short INVALID_ARGUMENTS                      = 0x0004	;
		public static final short ITEM_NOT_STORED                        = 0x0005	;
		public static final short INCR_DECR_ON_NON_NUMERIC_VALUE        = 0x0006	;
		public static final short THE_VBUCKET_BELONGS_TO_ANOTHER_SERVER  = 0x0007	;
		public static final short AUTHENTICATION_ERROR                   = 0x0008	;
		public static final short AUTHENTICATION_CONTINUE                = 0x0009	;
		public static final short UNKNOWN_COMMAND                        = 0x0081	;
		public static final short OUT_OF_MEMORY                          = 0x0082	;
		public static final short NOT_SUPPORTED                          = 0x0083	;
		public static final short INTERNAL_ERROR                         = 0x0084	;
		public static final short BUSY                                   = 0x0085	;
		public static final short TEMPORARY_FAILURE                      = 0x0086	;

	
	}
	
	public interface DataTypes {
		public static final byte RAW_BYES = (byte)0x00;
	}
}
