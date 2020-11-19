package halo.mzh.cache.serializer.support;

import com.esotericsoftware.kryo.kryo5.Kryo;
import com.esotericsoftware.kryo.kryo5.io.Input;
import com.esotericsoftware.kryo.kryo5.io.Output;
import org.apache.commons.lang3.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author shoufeng
 */

public class KryoSerializerSupport {

    private static volatile KryoSerializerSupport kryoSupport;

    public static KryoSerializerSupport instance() {

        if (ObjectUtils.isEmpty(kryoSupport)) {
            synchronized (KryoSerializerSupport.class) {
                if (ObjectUtils.isEmpty(kryoSupport)) {
                    kryoSupport = new KryoSerializerSupport();
                    return kryoSupport;
                }
            }
        }

        return kryoSupport;
    }

    public static KryoSerializerSupport newInstance() {

        return new KryoSerializerSupport();
    }

    private ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() -> {

        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);

        return kryo;

    });


    public byte[] encode(Object obj) throws IOException {

        Kryo kryo = kryoThreadLocal.get();

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); Output output = new Output(bos);) {

            kryo.writeClassAndObject(output, obj);
            output.close();

            return bos.toByteArray();
        }

    }

    public Object decode(byte[] bytes) throws IOException {

        Kryo kryo = kryoThreadLocal.get();

        try (Input input = new Input(new ByteArrayInputStream(bytes));) {

            Object obj = kryo.readClassAndObject(input);
            input.close();

            return obj;
        }
        
    }

}
