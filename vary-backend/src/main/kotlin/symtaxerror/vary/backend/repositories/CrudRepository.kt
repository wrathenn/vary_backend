package symtaxerror.vary.backend.repositories

interface CrudRepository<T, ID> {
    fun save(model: T): T
    fun saveAll(models: Iterable<T>): Iterable<T>

    fun delete(model: T)
    fun deleteAll(models: Iterable<T>)
    fun deleteById(id: ID)
    fun deleteAllById(ids: Iterable<ID>)

    fun findAll(): Iterable<T>
    fun findById(id: ID): T?
    fun findAllById(ids: Iterable<ID>): Iterable<T>

    fun existsById(id: ID): Boolean
    fun existsAllById(ids: Iterable<ID>): Boolean
    fun count(): Long
}