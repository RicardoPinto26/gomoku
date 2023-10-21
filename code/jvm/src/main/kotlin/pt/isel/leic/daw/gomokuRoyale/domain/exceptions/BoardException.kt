package pt.isel.leic.daw.gomokuRoyale.domain.exceptions

class UserNotInBoard(message: String) : Exception(message)

class BoardIsBoardWin(message: String) : Exception(message)

class BoardIsBoardDraw(message: String) : Exception(message)

class NotYourTurn(message: String) : Exception(message)
