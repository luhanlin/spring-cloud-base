package com.luhanlin.dbmongo;

import lombok.Data;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;

/**
 * @author neo
 */
@Data
public class MultipleMongoProperties {

	private MongoProperties primary = new MongoProperties();
	private MongoProperties secondary = new MongoProperties();


}
