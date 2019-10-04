package pl.north93.serializer.msgpack;

import org.msgpack.core.MessageBufferPacker;
import org.msgpack.core.MessagePack;

import pl.north93.serializer.platform.FieldInfo;
import pl.north93.serializer.platform.context.SerializationContext;
import pl.north93.serializer.platform.template.TemplateEngine;

public class MsgPackSerializationContext extends SerializationContext
{
    private final MessageBufferPacker packer = MessagePack.newDefaultBufferPacker();

    public MsgPackSerializationContext(final TemplateEngine templateEngine)
    {
        super(templateEngine);
    }

    public MessageBufferPacker getPacker()
    {
        return this.packer;
    }

    @Override
    public void enterObject(final FieldInfo field)
    {
    }

    @Override
    public void exitObject(final FieldInfo field)
    {
    }

    @Override
    public Object finalizeAndGetResult() throws Exception
    {
        this.packer.close();
        return this.packer.toByteArray();
    }

    @Override
    public void writeNull(final FieldInfo field) throws Exception
    {
        this.packer.packNil();
    }

    @Override
    public void writeString(final FieldInfo field, final String string) throws Exception
    {
        this.packer.packString(string);
    }

    @Override
    public void writeBoolean(final FieldInfo field, final Boolean aBoolean) throws Exception
    {
        this.packer.packBoolean(aBoolean);
    }

    @Override
    public void writeByte(final FieldInfo field, final Byte aByte) throws Exception
    {
        this.packer.packByte(aByte);
    }

    @Override
    public void writeShort(final FieldInfo field, final Short aShort) throws Exception
    {
        this.packer.packShort(aShort);
    }

    @Override
    public void writeInteger(final FieldInfo field, final Integer integer) throws Exception
    {
        this.packer.packInt(integer);
    }

    @Override
    public void writeFloat(final FieldInfo field, final Float aFloat) throws Exception
    {
        this.packer.packFloat(aFloat);
    }

    @Override
    public void writeDouble(final FieldInfo field, final Double aDouble) throws Exception
    {
        this.packer.packDouble(aDouble);
    }

    @Override
    public void writeLong(final FieldInfo field, final Long aLong) throws Exception
    {
        this.packer.packLong(aLong);
    }
}
