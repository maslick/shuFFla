*shuFFler*
------------------------------------


**Installation**
```
$ ./gradlew build
```
```
$ tree build/
├── distributions
│   ├── shuffler-0.1.tar
│   └── shuffler-0.1.zip
├── install
│   └── shuffler
│       ├── bin
│       │   ├── shuffler
│       │   └── shuffler.bat
│       └── lib
│           ├── javax.json-1.0.4.jar
│           ├── javax.persistence-2.1.1.jar
│           ├── lombok-1.16.8.jar
│           ├── mp3agic-0.8.4.jar
│           ├── org.eclipse.persistence.antlr-2.6.3.jar
│           ├── org.eclipse.persistence.asm-2.6.3.jar
│           ├── org.eclipse.persistence.core-2.6.3.jar
│           ├── org.eclipse.persistence.jpa-2.6.3.jar
│           ├── org.eclipse.persistence.jpa.jpql-2.6.3.jar
│           ├── shuffler-0.1.jar
│           └── sqlite-jdbc-3.8.11.2.jar
```

**Run**
```
$ cd build/install/shuffler/bin
$ export KOSMOS_MUSIC="/Users/user/Music"
$ ./shuffler
```

**API**
```
/getList              -- get a JSON list of all songs

/getList/{rhythm}     -- get a JSON list of songs with {rhythm} = 1..5

/getPls               -- get a text representation of a .pls file with all songs in

/getPls.pls           -- get a .pls file with all songs

/getPls/{rhythm}      -- get a text representation of a .pls file with songs of a certain {rhythm} = 1..5

/getPls/{rhythm}.pls  -- get a .pls file with songs of certain {rhythm} = 1..5
```
