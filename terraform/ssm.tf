resource "aws_ssm_parameter" "db_uri" {
  name  = "/ecs/api-franquicias-nequi/DB_URI"
  type  = "String"
  value = "${aws_db_instance.franquicias.endpoint}:${aws_db_instance.franquicias.port}"

  depends_on = [aws_db_instance.franquicias]
}
