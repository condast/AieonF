package org.aieonf.commons.base64;

import org.aieonf.commons.io.IOUtils;
import org.aieonf.concept.core.Descriptor;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64Utils
{

	public static String getBase64DataURI( Class<?> clss, String location ){
		InputStream in = clss.getResourceAsStream(location );
		try{
			return getBase64DataURI( in );
		}
		finally{
			IOUtils.closeQuietly( in );
		}
	}

	/**
	 * Get base64 encoded bytes from the given local path
	 * @param localPath
	 * @return
	 */
	public static String getBase64DataURI( InputStream in ){
		ByteArrayOutputStream buf = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];
		try{
			while ((nRead = in.read(data, 0, data.length)) != -1) {
				buf.write(data, 0, nRead);
			}
			buf.flush();
			in.close();
			String str = new String( Base64.encodeBase64(buf.toByteArray() ), "UTF-8");
			if( Descriptor.assertNull( str ))
				return null;
			return "data:image/png;base64," + str;
		}
		catch( Exception ex )
		{
			ex.printStackTrace();
		}
		finally{
			try {
				buf.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static boolean isBase64( byte[] bytes ){
			return Base64.isBase64( bytes );
	}

}
