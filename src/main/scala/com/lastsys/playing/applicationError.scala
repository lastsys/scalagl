package com.lastsys.playing

sealed trait ApplicationError
case class InitializationError(msg: String) extends ApplicationError
