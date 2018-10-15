package com.luhanlin.dbmongo;

import com.mongodb.MongoClientURI;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

@Configuration
@RefreshScope
public class MultipleMongoConfig {

    @Primary
	@Bean(name="mongoPrimary")
	@ConfigurationProperties(prefix = "mongodb.primary")
	@RefreshScope
	public MongoProperties primary(){
		return new MongoProperties();
	}

	@Bean(name="mongoSecondary")
	@ConfigurationProperties(prefix = "mongodb.secondary")
	@RefreshScope
	public MongoProperties secondary(){
		return new MongoProperties();
	}


    @Primary
    @Bean(name = PrimaryMongoConfig.MONGO_TEMPLATE)
    @RefreshScope
    public MongoTemplate primaryMongoTemplate(@Qualifier(value = "mongoPrimary")MongoProperties mongoProperties) throws Exception {
        System.out.println("mongoPrimary>>>>>>>>>>>>>>>>>>>"+mongoProperties.getHost());
        return new MongoTemplate(initFactory(mongoProperties));
    }

    @Bean(SecondaryMongoConfig.MONGO_TEMPLATE)
    @RefreshScope
    public MongoTemplate secondaryMongoTemplate(@Qualifier(value = "mongoSecondary")MongoProperties mongoProperties) throws Exception {
        System.out.println("mongoSecondary>>>>>>>>>>>>>>>>>>" + mongoProperties.getHost());
        return new MongoTemplate(initFactory(mongoProperties));
    }

    public SimpleMongoDbFactory initFactory(MongoProperties mongo) throws Exception {

        MongoClientURI mcu = new MongoClientURI(mongo.getUri());
        return new SimpleMongoDbFactory(mcu);
    }

}
