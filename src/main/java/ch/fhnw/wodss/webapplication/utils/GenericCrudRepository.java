package ch.fhnw.wodss.webapplication.utils;

import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.UpdatableRecord;
import org.jooq.impl.TableImpl;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

// Inspired by: https://github.com/jOOQ/jOOQ/issues/5984
public abstract class GenericCrudRepository<DTO, Record extends UpdatableRecord<Record>, Table extends TableImpl<Record>> {

    private final DSLContext dslContext;

    private final Converter converter;

    private final Table table;

    public GenericCrudRepository(DSLContext dslContext, Converter converter, Table table) {
        this.dslContext = dslContext;
        this.converter = converter;
        this.table = table;
    }

    protected Optional<DTO> createOne(DTO dto) {
        try {
            Record record = mapDtoToRecord(dto, dslContext.newRecord(table));
            record.store();
            return Optional.of(mapRecordToDto(record));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    protected List<DTO> readMany(Function<Table, Condition> function) {
        List<Record> records = dslContext.fetch(table, function.apply(table));
        return records.stream().map(this::mapRecordToDto).collect(Collectors.toList());
    }

    protected Optional<DTO> readOne(Function<Table, Condition> function) {
        try {
            Record record = dslContext.fetchOne(table, function.apply(table));
            return Optional.of(mapRecordToDto(record));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    protected Optional<DTO> updateOne(DTO dto) {
        try {
            Record record = mapDtoToRecord(dto, dslContext.newRecord(table));
            record.update();
            return Optional.of(mapRecordToDto(record));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    protected void deleteMany(Function<Table, Condition> function) {
        dslContext.delete(table).where(function.apply(table)).execute();
    }

    protected DSLContext getDslContext() {
        return dslContext;
    }

    protected Converter getConverter() {
        return converter;
    }

    protected abstract Record mapDtoToRecord(DTO dto, Record record);

    protected abstract DTO mapRecordToDto(Record record);
}
