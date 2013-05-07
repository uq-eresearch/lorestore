# lorestore
---

The Open Annotation lorestore Web Application was developed at the UQ ITEE eResearch Lab: http://itee.uq.edu.au/~eresearch

## About


The webapp provides services for storing Open Annotation (OA) annotations and ORE Resource Map bodies and targets.
We use the web app for our Open Annotation service at 
[http://openannotation.metadata.net/lorestore/](http://austese.net/lorestore/)

The source code for the lorestore webapp is available from [GitHub](https://github.com/uq-eresearch/lorestore/)

A pre-packaged war file and instructions for getting it up and running are aviailable from the [wiki on GitHub](https://github.com/uq-eresearch/lorestore/wiki).


## Packaging

Apache Maven is used for dependency management and building the web app. 

The web app can be configured via  override.properties, substitution.properties and xml files in WEB-INF/
directly or by a local override file (see Deployment), or these files can be overridden within a Maven war overlay.

We use eclipse to import lorestore into the workspace as a Maven project (using eGit and m2eclipse). 
We build the war file via an eclipse Maven run configuration equivalent to the following command:
mvn clean package

To configure the eclipse run configuration, set basedir to `${workspace_loc:/lorestore}` and goals to `clean package`.

The resulting war file will be `lorestore/target/lorestore.war`


## Deployment

You will need to create a directory to which the web app server has write access, for storing the lorestore Sesame RDF repository.

lorestore provides two alternatives for user authentication: using drupal accounts or local accounts (using DIAS-B Emmet to manage authentication and authorisation). By default lorestore is configured to use local Emmet accounts. When lorestore is configured to use local accounts, Emmet requires a MySQL database, which must be created with appropriate access permissions before deploying the war. Emmet creates required tables when run for the first time.

### Steps
Basic steps to deploy lorestore using default configuration on Unix-based systems (e.g. Linux, Mac OSX):

1. If using local user accounts, create a mysql database 'lorestore' accessed by user 'lorestore' using password 'lorestorepwd'

`mysql> create database lorestore;`
`mysql> grant all privileges on lorestore.* to 'lorestore'@'localhost' identified by 'lorestorepwd'; `

2. Create directory `/usr/share/lorestore` with appropriate file permissions

3. Deploy war file to Apache Tomcat running on localhost port 8080

### Customising 
The location of the data directory, authentication type, Emmet database name and access details, default hostname and port etc are configured through properties in the substitution.properties file in `src/main/webapp/WEB-INF/classes`

To override property values after the war file has been built, create a file named 'local-lorestore-substitution.properties' containing the new values, and place it in the Tomcat lib directory.
 
## User accounts

Default user accounts are configured in `src/main/webapp/WEB-INF/Emmet-userDetails-lorestore.xml`
These accounts will be created by Emmet on first run, and by default are ignored on subsequent deployments. 
Refer to the `newTables` property in the userDetails configuration file to modify this behaviour.
Use the User Management tools within the web app to create or configure user accounts.

If using drupal accounts, the [austese_repository](https://github.com/uq-eresearch/austese_repository) module is required.

## Backup

Use `mysqldump` to backup Emmet user data.

Use lorestore's `admin/export` page to export the RDF repository that stores OA annotations and Resource Maps to file. 
The file format supported for export and import is Trig; resource maps and annotations are stored as Named Graphs.

## History

This web app is derivative of the [Aus-e-Lit webapp](https://github.com/auselit/webapp), by the same developers:


[![Bitdeli Badge](https://d2weczhvl823v0.cloudfront.net/uq-eresearch/lorestore/trend.png)](https://bitdeli.com/free "Bitdeli Badge")

