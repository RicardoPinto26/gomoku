package pt.isel.leic.daw.gomokuRoyale.domain.exceptions

class UserNotInGame(message: String) : Exception(message)

class BoardWrongType(message: String) : Exception(message)

class UserWrongTurn(message: String) : Exception(message)

class InvalidPosition(message: String) : Exception(message)

class PositionAlreadyPlayed(message: String) : Exception(message)

class UserInvalidUsername(message: String) : Exception(message)

class UserInvalidEmail(message: String) : Exception(message)

class UserInvalidId(message: String) : Exception(message)

class UserInvalidPassword(message: String) : Exception(message)
