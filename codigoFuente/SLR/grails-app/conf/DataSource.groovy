dataSource {
    pooled = true
    driverClassName = "com.mysql.jdbc.Driver"
	//dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
	dialect = "es.uca.pfc.ImprovedMySQLDialect"
	username = "root"
    password = "admin"
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    //cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory'
	cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory'
}
// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            //url = "jdbc:mysql://localhost/bbddSRL?useUnicode=yes&characterEncoding=UTF-8"
			url = "jdbc:mysql://localhost:3306/pfcslr"
			username="root"
			password="admin"
        }
    }
    test {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            //url = "jdbc:mysql://localhost/bbddSRL?useUnicode=yes&characterEncoding=UTF-8"
			url = "jdbc:mysql://localhost:3306/pfcslr"
			username="root"
			password="admin"
        }
    }
    production {
        /*dataSource {
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            //url = "jdbc:mysql://localhost/bbddSRLprod?useUnicode=yes&characterEncoding=UTF-8"
			url = "jdbc:mysql://localhost:3306/pfcslrprod"
			username="root"
			password="admin"
        }*/
		dataSource
		{
			username = "mysql087"
			password = "mysql087"
			pooled = true
			dbCreate = "update"
			driverClassName = "com.mysql.jdbc.Driver"
			//url = "jdbc:mysql://aa1rnm4wvx4fwh2.clrt4hdjew73.us-east-1.rds.amazonaws.com:3306/ebdb?user=mysql087&password=mysql087"
			url = "jdbc:mysql://aa1rnm4wvx4fwh2.clrt4hdjew73.us-east-1.rds.amazonaws.com:3306/ebdb"
			dialect = org.hibernate.dialect.MySQL5InnoDBDialect
			properties
			{
				validationQuery = "SELECT 1"
				testOnBorrow = true
				testOnReturn = true
				testWhileIdle = true
				timeBetweenEvictionRunsMillis = 1800000
				numTestsPerEvictionRun = 3
				minEvictableIdleTimeMillis = 1800000
			}
		}
    }
}

production 
{
	dataSource 
	{
		username = "mysql087"
		password = "mysql087"
		pooled = true
		dbCreate = "update"
		driverClassName = "com.mysql.jdbc.Driver"
		//url = "jdbc:mysql://aa1rnm4wvx4fwh2.clrt4hdjew73.us-east-1.rds.amazonaws.com:3306/ebdb?user=mysql087&password=mysql087"
		url = "jdbc:mysql://aa1rnm4wvx4fwh2.clrt4hdjew73.us-east-1.rds.amazonaws.com:3306/ebdb"
		dialect = org.hibernate.dialect.MySQL5InnoDBDialect
		properties
		{
			validationQuery = "SELECT 1"
			testOnBorrow = true
			testOnReturn = true
			testWhileIdle = true
			timeBetweenEvictionRunsMillis = 1800000
			numTestsPerEvictionRun = 3
			minEvictableIdleTimeMillis = 1800000
		}
	}
}
