## Usage

1. Build (with Maven):
```
mvn package
```

2. Create a configuration file in the following format:
```
<minutes> <command>
<minutes> <command>
...
```

3. Prepare an SQLite database (if necessary):
```
sqlite3 <database> "CREATE TABLE log (date TEXT, command TEXT, result TEXT, details TEXT)"
```

4. Start the scheduler:
```
java -jar target/cronlike.jar <config> <log> [<database>]
```
* where:
  - `<config>` - configuration file
  - `<log>` - file for logging (will be created if absent)
  - `<database>` - database for logging (optional)


## Example

This line in the configuration file
```
40 mktemp -p ~
```
creates a new temporary file in the home directory each hour, specifically at 00h40m, 01h40m, 02h40m, etc.


## Log format

Separate log events occur on start and finish of a command.

### File

Each line contains the following fields separated by spaces:
* _date_ as `yyyy-MM-dd`
* _time_ as `HH:mm:ss`
* _command_ in brackets `[]`
* _result_ which is one of:
  - `START-OK` - started successfully
  - `START-ERROR` - could not start
  - `FINISH-OK` - finished successfully
  - `FINISH-ERROR` - finished with error
* _details_ of the error:
  - for `START-OK` - empty
  - for `START-ERROR` - text of the exception thrown on start
  - for `FINISH-OK` - empty
  - for `FINISH-ERROR` - exit code of the command

For example:
```
2020-10-26 21:10:00 [mktemp -p /etc] START-OK 
2020-10-26 21:10:00 [mktemp -p /etc] FINISH-ERROR 1
```

### Database

A log event inserts a new row in the table `log` that has these columns:
* `date    TEXT` as `yyyy-MM-dd HH:mm:ss`
* `command TEXT` as is
* `result  TEXT` - same as in file
* `details TEXT` - same as in file
