variable "aws_region" {
  description = "Región de AWS para el despliegue"
  type        = string
  default     = "us-east-1"
}

variable "vpc_id" {
  description = "ID de la VPC existente"
  type        = string
  default     = "vpc-04a0d9f6efd8cea41"
}

variable "subnet_ids" {
  description = "IDs de las subredes existentes para ECS y ALB"
  type        = list(string)
  default     = ["subnet-03b0a4d0333a3419e", "subnet-0dab25d7b8dcb5c72"]
}
