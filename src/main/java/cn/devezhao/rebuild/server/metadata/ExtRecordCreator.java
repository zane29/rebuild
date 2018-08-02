package cn.devezhao.rebuild.server.metadata;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;

import cn.devezhao.commons.CalendarUtils;
import cn.devezhao.persist4j.Entity;
import cn.devezhao.persist4j.Record;
import cn.devezhao.persist4j.engine.ID;
import cn.devezhao.persist4j.record.JsonRecordCreator;
import cn.devezhao.rebuild.server.Application;
import cn.devezhao.rebuild.server.service.user.UserService;

/**
 * @author Zhao Fangfang
 * @version $Id: ExtRecordCreator.java 22 2013-06-26 12:15:01Z zhaoff@qidapp.com $
 * @since 1.0, 2013-6-26
 */
public class ExtRecordCreator extends JsonRecordCreator {
	
	// 公共字段
	
	public static final String createdOn = "createdOn";
	public static final String createdBy = "createdBy";
	public static final String modifiedOn = "modifiedOn";
	public static final String modifiedBy = "modifiedBy";
	public static final String owningUser = "owningUser";
	public static final String owningDept = "owningDept";

	/**
	 * @param entity
	 * @param source
	 * @param editor
	 */
	public ExtRecordCreator(Entity entity, JSONObject source, ID editor) {
		super(entity, source, editor);
	}
	
	@Override
	protected void afterCreate(Record record, boolean isNew) {
		super.afterCreate(record, isNew);
		bindCommonsFieldsValue(record, isNew);
	}
	
	/**
	 * 绑定公用/权限字段值
	 * 
	 * @param r
	 * @param isNew
	 */
	protected static void bindCommonsFieldsValue(Record r, boolean isNew) {
		final Date now = CalendarUtils.now();
		final Entity entity = r.getEntity();
		
		if (entity.containsField(modifiedOn)) {
			r.setDate(modifiedOn, now);
		}
		if (entity.containsField(modifiedBy)) {
			r.setID(modifiedBy, r.getEditor());
		}
		
		if (isNew) {
			if (entity.containsField(createdOn)) {
				r.setDate(createdOn, now);
			}
			if (entity.containsField(createdBy)) {
				r.setID(createdBy, r.getEditor());
			}
			if (entity.containsField(owningUser)) {
				r.setID(owningUser, Application.getBean(UserService.class).getDeptOfUser(r.getEditor()));
			}
		}
	}
}