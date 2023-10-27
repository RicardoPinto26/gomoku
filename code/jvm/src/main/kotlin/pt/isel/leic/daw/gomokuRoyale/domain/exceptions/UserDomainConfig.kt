package pt.isel.leic.daw.gomokuRoyale.domain.exceptions

class TokenSizeZeroOrNegative(message: String) : Exception(message)

class TokenTTLNegative(message: String) : Exception(message)

class TokenRollingTTLNegative(message: String) : Exception(message)

class UserCantHaveTokens(message: String) : Exception(message)
