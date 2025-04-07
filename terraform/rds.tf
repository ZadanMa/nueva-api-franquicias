resource "aws_db_subnet_group" "franquicias_subnet_group" {
  name       = "franquicias-subnet-group"
  subnet_ids = var.subnet_ids

  tags = {
    Name = "franquicias-subnet-group"
  }
}

resource "aws_db_instance" "franquicias" {
  identifier             = "franquiciasdb"
  engine                 = "mysql"
  engine_version         = "8.0"
  instance_class         = "db.t3.micro"
  allocated_storage      = 20
  storage_type           = "gp2"
  db_subnet_group_name   = aws_db_subnet_group.franquicias_subnet_group.name
  vpc_security_group_ids = ["sg-0a3c90e0f2308aee7"]
  username               = data.aws_ssm_parameter.db_username.value
  password               = data.aws_ssm_parameter.db_password.value
  db_name                = "franquiciasdb"
  port                   = 3306
  publicly_accessible    = true

  skip_final_snapshot = true

  tags = {
    Name = "franquicias-db"
  }
}
