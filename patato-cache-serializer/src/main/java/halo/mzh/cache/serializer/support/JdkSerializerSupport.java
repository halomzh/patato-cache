package halo.mzh.cache.serializer.support;


import org.apache.commons.lang3.ObjectUtils;

import java.io.*;

/**
 * @author shoufeng
 */

public class JdkSerializerSupport {

    private static volatile JdkSerializerSupport jdkCoderSupport;

    public static JdkSerializerSupport instance() {

        if (ObjectUtils.isEmpty(jdkCoderSupport)) {
            synchronized (JdkSerializerSupport.class) {
                if (ObjectUtils.isEmpty(jdkCoderSupport)) {
                    jdkCoderSupport = new JdkSerializerSupport();
                    return jdkCoderSupport;
                }
            }
        }

        return jdkCoderSupport;
    }

    public static JdkSerializerSupport newInstance() {

        return new JdkSerializerSupport();
    }


    public byte[] encode(Object obj) throws IOException {

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream objectOutputStream = new ObjectOutputStream(bos);) {

            objectOutputStream.writeObject(obj);

            return bos.toByteArray();
        }

    }

    public Object decode(byte[] bytes) throws IOException, ClassNotFoundException {

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));) {

            Object obj = objectInputStream.readObject();
            objectInputStream.close();

            return obj;
        }

    }

}
