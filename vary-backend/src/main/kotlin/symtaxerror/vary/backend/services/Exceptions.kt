package symtaxerror.vary.backend.services.exceptions

class EntityNotFoundException(message: String) : Exception(message)

class EntityAlreadyExistsException(message: String) : Exception(message)

class ApplicationException(message: String): Exception(message)