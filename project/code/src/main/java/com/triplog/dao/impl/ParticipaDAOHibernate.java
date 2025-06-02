package com.triplog.dao.impl;

import com.triplog.dao.ParticipaDAO;
import com.triplog.model.Participa;
import com.triplog.model.ParticipaId;

public abstract class ParticipaDAOHibernate extends GenericDAOHibernate<Participa, Long> implements ParticipaDAO {
    public ParticipaDAOHibernate(){
        super(Participa.class);
    }
}
