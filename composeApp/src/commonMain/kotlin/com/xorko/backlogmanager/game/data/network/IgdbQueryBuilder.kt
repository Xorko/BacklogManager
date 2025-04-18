package com.xorko.backlogmanager.game.data.network

class IgdbQueryBuilder {
    private var search: String? = null
    private val fields = mutableListOf<String>()
    private var whereClause: WhereClause? = null
    private var limit: Int? = null
    private var offset: Int? = null
    private var sort: String? = null

    fun search(value: String): IgdbQueryBuilder {
        this.search = value
        return this
    }

    fun fields(vararg values: String): IgdbQueryBuilder {
        fields.addAll(values)
        return this
    }

    fun where(clause: WhereClause): IgdbQueryBuilder {
        this.whereClause = clause
        return this
    }

    fun sort(value: String): IgdbQueryBuilder {
        sort = value
        return this
    }

    fun limit(value: Int): IgdbQueryBuilder {
        limit = value
        return this
    }

    fun offset(value: Int): IgdbQueryBuilder {
        offset = value
        return this
    }

    fun build(): String {
        val parts = mutableListOf<String>()

        search?.let { parts.add("""search "$it"""") }
        parts.add("fields ${if (fields.isNotEmpty()) fields.joinToString(", ") else "*"}")
        whereClause?.let { parts.add("where $it") }
        sort?.let { parts.add("sort $it") }
        limit?.let { parts.add("limit $it") }
        offset?.let { parts.add("offset $it") }

        return parts.joinToString(";\n", postfix = ";")
    }
}

sealed class WhereClause {
    data class Condition(
        val expr: String,
    ) : WhereClause() {
        override fun toString(): String = super.toString()
    }

    data class And(
        val left: WhereClause,
        val right: WhereClause,
    ) : WhereClause() {
        override fun toString(): String = super.toString()
    }

    data class Or(
        val left: WhereClause,
        val right: WhereClause,
    ) : WhereClause() {
        override fun toString(): String = super.toString()
    }

    override fun toString(): String =
        when (this) {
            is Condition -> expr
            is And -> "($left & $right)"
            is Or -> "($left | $right)"
        }

    companion object {
        fun condition(expr: String) = Condition(expr)

        fun and(
            left: WhereClause,
            right: WhereClause,
        ) = And(left, right)

        fun or(
            left: WhereClause,
            right: WhereClause,
        ) = Or(left, right)
    }
}
