package server;

import java.io.*;

public class MessageReader2 {
	private static final int LENGTH_LENGTH = 4; // length of the length field, bytes	

	public static char[] readMessage(InputStream in) throws java.io.IOException, TerminateException, ContinueException {
		int readBytes = 0;
		int tmp, tmp_shift;
		int length = 0;

		while (readBytes < LENGTH_LENGTH) {
			tmp = in.read();
			++readBytes;
			tmp_shift = tmp << (LENGTH_LENGTH - readBytes);
			length |= tmp_shift;
		}
		if (length < 0) {
			trace("the client is fucking w/ us. Alternativly the connection died");
			throw new TerminateException();
		} else if (readBytes == LENGTH_LENGTH) {
			trace("the msg is " + length + " bytes long");
		} else {
			trace("failed to read length field");
			throw new ContinueException();
		}
		// got length, do work.
		InputStreamReader reader = new InputStreamReader(in);
		char[] message = new char[length];
		int ret;
		int offset = 0;
		while (offset < length) {
			ret = reader.read(message, offset, (length - offset));

			if (ret == -1) {
				trace("fuck. something went south. breaking the parsing of message.");
				throw new ContinueException();
			}
			offset += ret;
		}
		return message;
	}

	private static void trace(String msg) {
		System.out.println("Trace---> " + msg);
	}
}
