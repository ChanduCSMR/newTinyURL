package com.tinyurl.demo.daoimpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.tinyurl.demo.constant.TinyUrlConstant;
import com.tinyurl.demo.dao.TinyDao;
import com.tinyurl.demo.dto.TinyResponseDto;
import com.tinyurl.demo.entity.TinyUrlMaster;

@Repository
@Transactional
public class TinyDaoImpl implements TinyDao {

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public TinyResponseDto checkForDuplicate(String originalUrl) {

		TinyResponseDto tinyResponseDto = new TinyResponseDto();
		try {
			String tinyUrl = (String) entityManager.unwrap(Session.class)
					.createQuery(
							" select tinyUrl from TinyUrlMaster where originalUrl =:originalUrl and status=:status")
					.setParameter("originalUrl", originalUrl).setParameter("status", TinyUrlConstant.ACTIVE_STATUS)
					.uniqueResult();
			if (tinyUrl == null) {
				return null;
			} else {
				Date dt = new Date();
				Calendar c = Calendar.getInstance();
				c.setTime(dt);
				c.add(Calendar.DATE, 1);
				dt = c.getTime();
				DateFormat df = new SimpleDateFormat("MM/dd/yyyy 00:00:00");
				String reportDate = df.format(dt);
				tinyResponseDto.setResult(TinyUrlConstant.SUCCESS_STATUS);
				tinyResponseDto.setUrl(tinyUrl);
				tinyResponseDto.setValidUpto(reportDate);
				return tinyResponseDto;
			}
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean updateTinyUrl(TinyUrlMaster tinyUrlMaster) {
		try {
			entityManager.unwrap(Session.class).save(tinyUrlMaster);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// Disable all the url at mid night
	@Override
	public void disableTinyURL() {
		try {
			entityManager.unwrap(Session.class)
					.createQuery("update TinyUrlMaster set status =:disable where status =:enable")
					.setParameter("disable", TinyUrlConstant.EXPIRED_STATUS)
					.setParameter("enable", TinyUrlConstant.ACTIVE_STATUS).executeUpdate();
		} catch (Exception e) {
			// mail/notification triggering code after successfully updated or after getting
			// error
			e.printStackTrace();
		}
	}

	@Override
	public TinyUrlMaster getUrlByUniqueId(long tinyUrlId) {

		try {
			TinyUrlMaster tinyUrlMaster = (TinyUrlMaster) entityManager.unwrap(Session.class)
					.createQuery("from TinyUrlMaster where uniqueNumber =:uniqueNumber")
					.setParameter("uniqueNumber", tinyUrlId).uniqueResult();
			return tinyUrlMaster;
		} catch (Exception e) {
			return null;
		}
	}

}
