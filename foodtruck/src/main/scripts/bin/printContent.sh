EXEC_SOURCE=“${BASH_SOURCE[0]}”;
EXEC_DIR=“”dirname $EXEC_SOURCE;

cd $EXEC_DIR/..;

CMD_CLASSPATH=“.”:”./conf”;

for jarFile in lib/*.jar
do
export CMD_CLASSPATH=$CMD_CLASSPATH:$jarFile;
done

CMD=“java -cp $CMD_CLASSPATH com.halfpenny.mars.jmx.client.HalfpennyJMXPrint”

echo $CMD;
$CMD;