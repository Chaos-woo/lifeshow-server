package per.chao.lifeshow.init;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * Description: 表结构和数据初始化
 * 初始化顺序：
 * 初始化系统权限
 * 初始化超级管理员
 * 初始化管理员操作日志类型
 * 初始化短视频状态类型
 * 初始化用户消息类型
 *
 * @author W.Chao
 * @date 2020/3/2 15:56
 **/
@Configuration
public class BatchTaskDataSourceInitializer {
	@Value("${classpath:sql-schema}")
	private Resource businessSchemaScript;
	@Value("${classpath:sql-data}")
	private Resource businessDataScript;

	@Bean
	public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
		final DataSourceInitializer initializer = new DataSourceInitializer();
		initializer.setDataSource(dataSource);
		initializer.setDatabasePopulator(databasePopulator());
		return initializer;
	}

	private DatabasePopulator databasePopulator() {
		final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(businessSchemaScript);
		populator.addScript(businessDataScript);
		return populator;
	}


}
