El error es que el **frontend-maven-plugin** está tratando de descargar Node.js pero la URL no existe. Ya tienes Node.js 22.12.0 instalado globalmente, así que podemos configurar el plugin para que use tu instalación existente.

## 🚨 **Problema Detectado**
La URL `https://nodejs.org/dist/22.12.0/win-x64/node.exe` devuelve 404 porque **Node.js 22.12.0 no existe** en esa ruta exacta. La versión correcta sería algo como `v22.12.0`.

## ✅ **Solución: Configurar frontend/pom.xml**

Actualiza tu `frontend/pom.xml`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>com.github.maximovj</groupId>
        <artifactId>rhhub</artifactId>
        <version>0.0.1-SNAPSHOT</version>
        <relativePath>../pom.xml</relativePath>
    </parent>
    
    <artifactId>frontend</artifactId>
    <name>rhhub-frontend</name>
    <packaging>pom</packaging>
    
    <properties>
        <!-- Usa una versión de Node.js que SÍ exista o desactiva descarga -->
        <node.version>v22.12.0</node.version> <!-- Con 'v' al inicio -->
        <npm.version>10.9.0</npm.version>
        <frontend-maven-plugin.version>1.15.0</frontend-maven-plugin.version>
    </properties>
    
    <build>
        <plugins>
            <plugin>
                <groupId>com.github.eirslett</groupId>
                <artifactId>frontend-maven-plugin</artifactId>
                <version>${frontend-maven-plugin.version}</version>
                <configuration>
                    <!-- OPCIÓN 1: Usar Node.js instalado (RECOMENDADO) -->
                    <installDirectory>${project.basedir}</installDirectory>
                    <nodeVersion>${node.version}</nodeVersion>
                    <npmVersion>${npm.version}</npmVersion>
                    <workingDirectory>./</workingDirectory>
                    
                    <!-- OPCIÓN 2: Desactivar instalación (usa el global) -->
                    <skip>true</skip> <!-- Solo si ya tienes Node.js instalado -->
                </configuration>
                <executions>
                    <!-- Instalar Node.js (solo si no existe) -->
                    <execution>
                        <id>install-node-npm</id>
                        <goals>
                            <goal>install-node-and-npm</goal>
                        </goals>
                        <!-- Si ya tienes Node.js, puedes saltar este paso -->
                        <phase>none</phase> <!-- Desactiva la ejecución -->
                    </execution>
                    
                    <!-- Instalar dependencias npm -->
                    <execution>
                        <id>npm-install</id>
                        <goals>
                            <goal>npm</goal>
                        </goals>
                        <configuration>
                            <arguments>install</arguments>
                        </configuration>
                    </execution>
                    
                    <!-- Construir Vue -->
                    <execution>
                        <id>npm-build</id>
                        <goals>
                            <goal>npm</goal>
                        </goals>
                        <configuration>
                            <arguments>run build</arguments>
                        </configuration>
                        <phase>prepare-package</phase>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

## 🔧 **Soluciones Alternativas**

### **Opción A: Cambiar a una versión LTS estable**
```xml
<properties>
    <!-- Usa versión LTS que SÍ existe -->
    <node.version>v20.19.0</node.version> <!-- Versión LTS estable -->
    <npm.version>10.2.4</npm.version>
</properties>
```

### **Opción B: Desactivar completamente la instalación (si ya tienes Node)**
```xml
<plugin>
    <groupId>com.github.eirslett</groupId>
    <artifactId>frontend-maven-plugin</artifactId>
    <version>1.15.0</version>
    <configuration>
        <!-- No instalar Node.js, usar el del sistema -->
        <skipInstall>true</skipInstall>
        <skip>true</skip>
    </configuration>
    <executions>
        <!-- Solo npm install y build -->
        <execution>
            <id>npm-install</id>
            <goals><goal>npm</goal></goals>
            <configuration>
                <arguments>install</arguments>
            </configuration>
        </execution>
        <execution>
            <id>npm-build</id>
            <goals><goal>npm</goal></goals>
            <configuration>
                <arguments>run build</arguments>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### **Opción C: Usar nodeDownloadRoot correcto**
```xml
<configuration>
    <nodeVersion>v22.12.0</nodeVersion> <!-- CON 'v' -->
    <nodeDownloadRoot>https://nodejs.org/dist/</nodeDownloadRoot>
    <npmVersion>10.9.0</npmVersion>
</configuration>
```

## 📋 **Pasos para Corregir**

### **PASO 1: Actualizar frontend/pom.xml**
Usa una de las opciones anteriores. Te recomiendo **Opción B** si ya tienes Node.js 22.12.0 instalado.

### **PASO 2: Verificar package.json**
Asegúrate de que `frontend/package.json` existe y tiene:
```json
{
  "scripts": {
    "dev": "vite",
    "build": "vite build",
    "preview": "vite preview"
  }
}
```

### **PASO 3: Ejecutar manualmente primero**
```bash
# Navegar al frontend
cd frontend

# Instalar dependencias npm manualmente
npm install

# Construir Vue
npm run build

# Volver y probar Maven
cd ..
mvn clean install -pl frontend
```

### **PASO 4: Solucionar warnings del backend**
Los warnings de dependencias duplicadas en backend:
```xml
<!-- En backend/pom.xml, revisa líneas ~77 y ~82 -->
<!-- Elimina dependencias duplicadas, ejemplo: -->
<dependencies>
    <!-- Si tienes mysql-connector-j DOS veces, elimina una -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <scope>runtime</scope>
        <!-- Solo UNA vez -->
    </dependency>
</dependencies>
```

## 🚀 **Comandos para Probar**

```bash
# 1. Solo frontend (con Node.js ya instalado)
mvn clean install -pl frontend -DskipTests

# 2. Forzar skip de instalación Node
mvn clean install -pl frontend -Dfrontend.skip.installnodenpm=true

# 3. Construir todo el proyecto
mvn clean install -DskipTests

# 4. Si solo quieres probar el build de Vue manualmente
cd frontend
npm run build
```

## ⚡ **Solución Rápida (Recomendada)**

Dado que **ya tienes Node.js 22.12.0 instalado**, modifica `frontend/pom.xml` así:

```xml
<plugin>
    <groupId>com.github.eirslett</groupId>
    <artifactId>frontend-maven-plugin</artifactId>
    <version>1.15.0</version>
    <configuration>
        <!-- Usa Node.js del sistema, no descargues -->
        <skip>true</skip>
        <workingDirectory>./</workingDirectory>
    </configuration>
    <executions>
        <!-- Solo estas dos ejecuciones -->
        <execution>
            <id>npm-install</id>
            <goals><goal>npm</goal></goals>
            <configuration>
                <arguments>install</arguments>
            </configuration>
        </execution>
        <execution>
            <id>npm-build</id>
            <goals><goal>npm</goal></goals>
            <configuration>
                <arguments>run build</arguments>
            </configuration>
            <phase>prepare-package</phase>
        </execution>
    </executions>
</plugin>
```

**¿Quieres que te ayude también con los warnings de dependencias duplicadas en el backend?** Comparte las líneas 70-85 de tu `backend/pom.xml` y te muestro exactamente qué eliminar.