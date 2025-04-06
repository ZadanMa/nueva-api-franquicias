data "aws_ssm_parameter" "db_username" {
  name = "/ecs/api-franquicias-nequi/DB_USERNAME"
}

data "aws_ssm_parameter" "db_password" {
  name = "/ecs/api-franquicias-nequi/DB_PASSWORD"
}

data "aws_ssm_parameter" "app_port" {
  name = "/ecs/api-franquicias-nequi/PORT"
}
