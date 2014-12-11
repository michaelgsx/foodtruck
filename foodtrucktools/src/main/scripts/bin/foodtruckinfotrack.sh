
CMD_CLASSPATH=".":"./conf";

for jarFile in lib/*.jar
do
export CMD_CLASSPATH=$CMD_CLASSPATH:$jarFile;
done

CMD="java -cp $CMD_CLASSPATH uber.foodtrucktools.TruckInfoCollector $*"

echo $CMD;
$CMD;