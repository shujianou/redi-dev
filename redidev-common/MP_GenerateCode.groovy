import com.intellij.database.model.DasTable
import com.intellij.database.model.ObjectKind
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil
import java.text.SimpleDateFormat

/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 */
packageDir="com.redimybase.security.manager"    //生成包根目录
idType="String"
extentdIdClass="MStringIdEntity"        //ID实体基类
//entityPackge = "com.redimybase.security.manager.entity;"
author = "Irany"
typeMapping = [
        (~/(?i)bigint/)                   : "String",       //主键类型,根据不同应用场景进行修改
        (~/(?i)int/)                      : "String",
        (~/(?i)float|double|decimal|real/): "Double",
        (~/(?i)datetime|timestamp/)       : "java.util.Date",
        (~/(?i)date/)                     : "java.sql.Date",
        (~/(?i)time/)                     : "java.sql.Time",
        (~/(?i)/)                         : "String"
]

FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated entity") { dir ->
    SELECTION.filter { it instanceof DasTable && it.getKind() == ObjectKind.TABLE }.each { generate(it, dir) }
}
FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated mapper") { dir ->
    SELECTION.filter { it instanceof DasTable && it.getKind() == ObjectKind.TABLE }.each { generateDao(it, dir) }
}
FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated service") { dir ->
    SELECTION.filter { it instanceof DasTable && it.getKind() == ObjectKind.TABLE }.each { generateService(it, dir) }
}

def generate(table, dir) {
    def className = javaName(table.getName(), true,"Entity")
    def classPackage=javaName(table.getName(), false,"")
    def tableName = table.getName()
    def fields = calcFields(table)
    def date = today()
    new File(dir, className + ".java").withPrintWriter { out -> generate(out, className,classPackage, fields, tableName, date) }
}

def generateDao(table, dir) {
    def className = javaName(table.getName(), true,"Dao")
    def entityName = javaName(table.getName(), true,"Entity")
    def classPackage=javaName(table.getName(), false,"")
    def tableName = table.getName()
    def date = today()
    new File(dir, className + ".java").withPrintWriter { out -> genDao(out, className, entityName,tableName, date,classPackage) }
}

def generateService(table, dir) {
    def className = javaName(table.getName(), true,"Service")
    def entityName = javaName(table.getName(), true,"Entity")
    def daoName=javaName(table.getName(),true,"Dao")
    def classPackage=javaName(table.getName(), false,"")
    def tableName = table.getName()
    def date = today()
    new File(dir, className + ".java").withPrintWriter { out -> genService(out, className, entityName,daoName,tableName, date,classPackage) }
}

/**
 * 生成DAO
 */
def genDao(out,className,entityName,tableName,date,classPackage){
    out.println "package $packageDir"+"."+classPackage+".mapper;"
    out.println ""
    out.println ""
    out.println "import com.baomidou.mybatisplus.mapper.BaseMapper;"
    out.println "import $packageDir"+".entity.$entityName;"
    out.println "/**"
    out.println " * $tableName Dao"
    out.println " * Created by $author $date"
    out.println " */"
    out.println "@Mapper"
    out.println "public interface $className extends BaseMapper<$entityName> {"
    out.println ""
    out.println"}"
}

/**
 * 生成Service
 */
def genService(out,className,entityName,daoName,tableName,date,classPackage){
    out.println "package $packageDir"+"."+classPackage+".service;"
    out.println ""
    out.println ""
    out.println "import com.redimybase.framework.jpa.service.BaseService;"
    out.println "import $packageDir"+".entity.$entityName;"
    out.println "import $packageDir"+".mapper.$daoName;"
    out.println "import com.baomidou.mybatisplus.service.impl.ServiceImpl;"
    out.println "import org.springframework.stereotype.Service;"
    out.println "/**"
    out.println " * $tableName Service"
    out.println " * Created by $author $date"
    out.println " */"
    out.println "@Service"
    out.println "public class $className extends ServiceImpl<$daoName,$entityName> {"
    out.println"}"
}

/**
 * 生成实体类
 */
def generate(out, className, fields, classPackage,tableName, date) {
    out.println "package $packageDir"+"."+classPackage+".entity;"
    out.println ""
    out.println ""
    out.println "import lombok.Data;"
    out.println "import com.baomidou.mybatisplus.activerecord.Model;"
    out.println "import com.baomidou.mybatisplus.annotations.TableId;"
    out.println "import com.baomidou.mybatisplus.annotations.TableName;"
    out.println ""
    out.println "/**"
    out.println " * $tableName Entity"
    out.println " * Created by $author $date"
    out.println " */"
    out.println "@TableName(value=" + "\"" + "$tableName" + "\"" + ")"
    out.println "@Data"
    out.println "public class $className  extends Model<$className> {"
    out.println ""
    fields.each() {
        if (it.annos != "") out.println "  ${it.annos}"
        if (it.name == "id"){
            out.println "@TableId"
            out.println " private ${it.type} ${it.name};"
        }else{
            out.println " private ${it.type} ${it.name};"
        }
    }
    out.println ""
    /* fields.each() {
         out.println ""
         out.println "  public ${it.type} get${it.name.capitalize()}() {"
         out.println "    return ${it.name};"
         out.println "  }"
         out.println ""
         out.println "  public void set${it.name.capitalize()}(${it.type} ${it.name}) {"
         out.println "    this.${it.name} = ${it.name};"
         out.println "  }"
         out.println ""
     }*/
    out.println "}"
}

def today() {
    String str = ""
    SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss")
    Calendar lastDate = Calendar.getInstance()
    str = sdf.format(lastDate.getTime())
    return str
}

def calcFields(table) {
    DasUtil.getColumns(table).reduce([]) { fields, col ->
        def spec = Case.LOWER.apply(col.getDataType().getSpecification())
        def typeStr = typeMapping.find { p, t -> p.matcher(spec).find() }.value
        fields += [[
                           name : javaName(col.getName(), false,""),
                           type : typeStr,
                           annos: ""]]
    }
}

def javaName(str, capitalize,type) {
    def s = com.intellij.psi.codeStyle.NameUtil.splitNameIntoWords(str)
            .collect { Case.LOWER.apply(it).capitalize() }
            .join("")
            .replaceAll(/[^\p{javaJavaIdentifierPart}[_]]/, "_")

    //capitalize || s.length() == 1 ? s : Case.LOWER.apply(s[0]) + s[1..-1]
    capitalize || s.length() == 1 ? s.replace("T", "") + type : Case.LOWER.apply(s[0]) + s[1..-1]
}
