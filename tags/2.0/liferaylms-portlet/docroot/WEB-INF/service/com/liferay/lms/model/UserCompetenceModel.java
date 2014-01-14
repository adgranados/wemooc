/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.lms.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.BaseModel;
import com.liferay.portal.model.CacheModel;
import com.liferay.portal.service.ServiceContext;

import com.liferay.portlet.expando.model.ExpandoBridge;

import java.io.Serializable;

import java.util.Date;

/**
 * The base model interface for the UserCompetence service. Represents a row in the &quot;Lms_UserCompetence&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation {@link com.liferay.lms.model.impl.UserCompetenceModelImpl} exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link com.liferay.lms.model.impl.UserCompetenceImpl}.
 * </p>
 *
 * @author TLS
 * @see UserCompetence
 * @see com.liferay.lms.model.impl.UserCompetenceImpl
 * @see com.liferay.lms.model.impl.UserCompetenceModelImpl
 * @generated
 */
public interface UserCompetenceModel extends BaseModel<UserCompetence> {
	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a user competence model instance should use the {@link UserCompetence} interface instead.
	 */

	/**
	 * Returns the primary key of this user competence.
	 *
	 * @return the primary key of this user competence
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this user competence.
	 *
	 * @param primaryKey the primary key of this user competence
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the uuid of this user competence.
	 *
	 * @return the uuid of this user competence
	 */
	@AutoEscape
	public String getUuid();

	/**
	 * Sets the uuid of this user competence.
	 *
	 * @param uuid the uuid of this user competence
	 */
	public void setUuid(String uuid);

	/**
	 * Returns the usercomp ID of this user competence.
	 *
	 * @return the usercomp ID of this user competence
	 */
	public long getUsercompId();

	/**
	 * Sets the usercomp ID of this user competence.
	 *
	 * @param usercompId the usercomp ID of this user competence
	 */
	public void setUsercompId(long usercompId);

	/**
	 * Returns the user ID of this user competence.
	 *
	 * @return the user ID of this user competence
	 */
	public long getUserId();

	/**
	 * Sets the user ID of this user competence.
	 *
	 * @param userId the user ID of this user competence
	 */
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this user competence.
	 *
	 * @return the user uuid of this user competence
	 * @throws SystemException if a system exception occurred
	 */
	public String getUserUuid() throws SystemException;

	/**
	 * Sets the user uuid of this user competence.
	 *
	 * @param userUuid the user uuid of this user competence
	 */
	public void setUserUuid(String userUuid);

	/**
	 * Returns the competence ID of this user competence.
	 *
	 * @return the competence ID of this user competence
	 */
	public long getCompetenceId();

	/**
	 * Sets the competence ID of this user competence.
	 *
	 * @param competenceId the competence ID of this user competence
	 */
	public void setCompetenceId(long competenceId);

	/**
	 * Returns the comp date of this user competence.
	 *
	 * @return the comp date of this user competence
	 */
	public Date getCompDate();

	/**
	 * Sets the comp date of this user competence.
	 *
	 * @param compDate the comp date of this user competence
	 */
	public void setCompDate(Date compDate);

	public boolean isNew();

	public void setNew(boolean n);

	public boolean isCachedModel();

	public void setCachedModel(boolean cachedModel);

	public boolean isEscapedModel();

	public Serializable getPrimaryKeyObj();

	public void setPrimaryKeyObj(Serializable primaryKeyObj);

	public ExpandoBridge getExpandoBridge();

	public void setExpandoBridgeAttributes(ServiceContext serviceContext);

	public Object clone();

	public int compareTo(UserCompetence userCompetence);

	public int hashCode();

	public CacheModel<UserCompetence> toCacheModel();

	public UserCompetence toEscapedModel();

	public String toString();

	public String toXmlString();
}