#!/usr/bin/env node
import { execSync } from "child_process";
import dotenv from "dotenv";
import path from "path";

dotenv.config();

const cmd = process.argv[2];

const backendDir = path.resolve("rhhub-backend");

const run = (command) => {
  execSync(command, {
    cwd: backendDir,
    stdio: "inherit",
    shell: true
  });
};

switch (cmd) {
  case "dev":
    run("mvn spring-boot:run -Dspring-boot.run.profiles=dev");
    break;

  case "seeder":
    run("mvn spring-boot:run -Dspring-boot.run.profiles=seeder");
    break;

  case "build":
    run("mvn clean package");
    break;

  case "test":
    run("mvn test");
    break;

  default:
    console.log(`
Uso:
  rhhub dev        Arranca backend en dev
  rhhub seeder     Arranca backend con seeders
  rhhub build      Compila el backend
  rhhub test       Ejecuta tests
`);
}
