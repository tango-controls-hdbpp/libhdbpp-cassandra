package fr.soleil.snapArchivingApi.SnapshotingApi.persistence.spring.dao;

import org.hibernate.SessionFactory;

import fr.soleil.snapArchivingApi.SnapshotingApi.persistence.spring.dto.ScStr2Val;

public class ScStr2ValDAOImpl extends AbstractValDAO<ScStr2Val> {
	public ScStr2ValDAOImpl(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	protected Class<ScStr2Val> getValueClass() {
		return ScStr2Val.class;
	}
}
