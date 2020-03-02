package pl.north93.serializer.mongodb.reader;

import java.util.Collection;
import java.util.ListIterator;

import org.bson.BsonArray;
import org.bson.BsonBinary;
import org.bson.BsonRegularExpression;
import org.bson.BsonType;
import org.bson.BsonValue;
import org.bson.types.ObjectId;

/*default*/ class ArrayNorthReadingContext extends NorthReadingContext
{
    private final ListIterator<BsonValue> iterator;

    public ArrayNorthReadingContext(final BsonArray array)
    {
        this.iterator = array.listIterator();
    }

    @Override
    public NorthReadingContext enterObject(final String name)
    {
        final BsonValue value = this.iterator.next();
        return new DocumentNorthReadingContext(value.asDocument());
    }

    @Override
    public NorthReadingContext enterArray(final String name)
    {
        final BsonValue value = this.iterator.next();
        return new ArrayNorthReadingContext(value.asArray());
    }

    @Override
    public boolean hasMore()
    {
        return this.iterator.hasNext();
    }

    @Override
    public boolean containsKey(final String name)
    {
        if (name == null)
        {
            return this.hasMore();
        }

        throw new IllegalStateException("There is no names in array so we can't check does key exists!");
    }

    @Override
    public Collection<String> getKeys()
    {
        throw new IllegalStateException("There is no names in array!");
    }

    @Override
    public BsonType readType(final String name)
    {
        try
        {
            final BsonValue next = this.iterator.next();
            return next.getBsonType();
        }
        finally
        {
            this.iterator.previous();
        }
    }

    @Override
    public String readString(final String name)
    {
        final BsonValue value = this.iterator.next();
        return value.asString().getValue();
    }

    @Override
    public boolean readBoolean(final String name)
    {
        final BsonValue value = this.iterator.next();
        return value.asBoolean().getValue();
    }

    @Override
    public int readInt32(final String name)
    {
        final BsonValue value = this.iterator.next();
        return value.asInt32().getValue();
    }

    @Override
    public double readDouble(final String name)
    {
        final BsonValue value = this.iterator.next();
        return value.asDouble().getValue();
    }

    @Override
    public long readInt64(final String name)
    {
        final BsonValue value = this.iterator.next();
        return value.asInt64().getValue();
    }

    @Override
    public ObjectId readObjectId(final String name)
    {
        final BsonValue value = this.iterator.next();
        return value.asObjectId().getValue();
    }

    @Override
    public BsonBinary readBinary(final String name)
    {
        final BsonValue value = this.iterator.next();
        return value.asBinary();
    }

    @Override
    public BsonRegularExpression readRegularExpression(final String name)
    {
        final BsonValue value = this.iterator.next();
        return value.asRegularExpression();
    }
}
