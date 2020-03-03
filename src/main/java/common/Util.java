package common;

import com.sun.xml.internal.bind.v2.util.ByteArrayOutputStreamEx;

import java.io.*;

public class Util {
    public static Object deepCopy (Object item) throws IOException, ClassNotFoundException {
        Object copied = null;

        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(item);
            oos.flush();
            ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            copied = (Object) ois.readObject();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (ois != null) {
                    ois.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }
        return  copied;

    }
}
