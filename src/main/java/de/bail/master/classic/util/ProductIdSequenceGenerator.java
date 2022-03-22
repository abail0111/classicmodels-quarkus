package de.bail.master.classic.util;

import de.bail.master.classic.model.enities.Product;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.LongType;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

public class ProductIdSequenceGenerator extends SequenceStyleGenerator {

    private String format;

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        super.configure(LongType.INSTANCE, params, serviceRegistry);
        this.format = "S%s_%d";
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String scale = ((Product) object).getProductScale().split(":")[1];
        return String.format(format, scale, super.generate(session, object));
    }
}
