package com.yango.generator;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

public class MyGenerator {

	public static String PARENT_PATH = "com.yango";

	public static String APPLICATION_CONFIG_PATH = "application-dev.properties";

	/**
	 * <p>
	 * MySQL 生成演示
	 * </p>
	 */
	public static void main(String[] args) {
		try {
			 generatorCode("uc", "uc_");
			 generatorCode("cif", "cif_");
			 generatorCode("as", "as_");
			 generatorCode("sys", "sys_");
			 
			 
			// generatorCode("crm", "crm_");
			// generatorCode("busi", "busi_");
			// generatorCode("sys", "sys_");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void generatorCode(String moduleName, String tablePrefix) throws Exception {
		// 代码生成器
		AutoGenerator mpg = new AutoGenerator();

		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		String projectPath = System.getProperty("user.dir");
		gc.setOutputDir(projectPath + "/src/main/java");
		gc.setAuthor("caoz");
		gc.setOpen(false);
		gc.setDateType(DateType.ONLY_DATE);
		gc.setEntityName("%sPO");
		gc.setServiceImplName("%sService");
		mpg.setGlobalConfig(gc);

		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		Properties prop = new Properties();
		prop.load(new FileInputStream(projectPath + "/src/main/resources/" + APPLICATION_CONFIG_PATH));

		dsc.setUrl(prop.getProperty("spring.datasource.url"));
		dsc.setDriverName(prop.getProperty("spring.datasource.driver-class-name"));
		dsc.setUsername(prop.getProperty("spring.datasource.username"));
		dsc.setPassword(prop.getProperty("spring.datasource.password"));
		mpg.setDataSource(dsc);

		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setModuleName(moduleName);
		pc.setParent(PARENT_PATH);
		pc.setServiceImpl("service");
		pc.setEntity("dao.model");
		pc.setMapper("dao.mapper");
		pc.setController("");
		mpg.setPackageInfo(pc);

		// 自定义配置
		InjectionConfig cfg = new InjectionConfig() {
			@Override
			public void initMap() {
				// to do nothing
			}
		};
		List<FileOutConfig> focList = new ArrayList<>();
		focList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
			@Override
			public String outputFile(TableInfo tableInfo) {
				// 自定义输入文件名称
				return projectPath + "/src/main/resources/mapper/" + pc.getModuleName() + "/" + tableInfo.getXmlName() + StringPool.DOT_XML;
			}
		});
		cfg.setFileOutConfigList(focList);
		mpg.setCfg(cfg);
		mpg.setTemplate(new TemplateConfig().setController(null).setService(null).setXml(null));

		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		strategy.setNaming(NamingStrategy.underline_to_camel);
		strategy.setColumnNaming(NamingStrategy.underline_to_camel);
		strategy.setSuperEntityClass(PARENT_PATH + ".common.dao.model.BasePO");
		strategy.setSuperServiceImplClass(PARENT_PATH + ".common.service.BaseService");
		strategy.setInclude(tablePrefix + ".*");
		strategy.setEntityLombokModel(false);
		strategy.setRestControllerStyle(true);
		strategy.setSuperControllerClass(null);

		strategy.setSuperEntityColumns("id", "is_valid", "version", "create_user", "create_date", "update_user", "update_date", "memo1", "memo2", "memo3");
		strategy.setControllerMappingHyphenStyle(true);
		strategy.setTablePrefix(tablePrefix);
		mpg.setStrategy(strategy);
		mpg.setTemplateEngine(new FreemarkerTemplateEngine());

		mpg.execute();
	}

}
