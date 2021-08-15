import org.codehaus.groovy.runtime.EncodingGroovyMethods

int manufacturerId = 0x0065
println(Integer.toHexString(manufacturerId))
String manufacturerIdString = String.format("%04x", manufacturerId)
String deviceManufacturingCode = "${manufacturerIdString}-${manufacturerIdString}-${manufacturerIdString}"
println(deviceManufacturingCode)
//println(EncodingGroovyMethods.encodeBase64(manufacturerId))
