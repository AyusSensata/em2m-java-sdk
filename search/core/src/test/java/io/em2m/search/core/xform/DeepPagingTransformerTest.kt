package io.em2m.search.core.xform

import io.em2m.search.core.model.*
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DeepPagingTransformerTest {

    private val xform = DeepPagingTransformer<Any?>("_id")

    @Test
    fun testMoSortsNoLast() {
        val req = SearchRequest(query = MatchAllQuery(), fields = listOf(Field("name"), Field("title"))).deepPage(true)
        val req2 = xform.transformRequest(req, emptyMap())
        val q2 = req2.query
        assertTrue(q2 is MatchAllQuery)
    }

    @Test
    fun testMoSortsWithLast() {
        val params = mapOf("lastKey" to mapOf("_id" to 1))
        val req = SearchRequest(
            query = MatchAllQuery(),
            fields = listOf(Field("name"), Field("title")),
            params = params
        ).deepPage(true)
        val req2 = xform.transformRequest(req, emptyMap())
        val q2 = req2.query
        assertTrue(q2 is RangeQuery)
        assertEquals(1, q2.gt)
    }

    @Test
    fun `request with one sort and a last value`() {
        val params = mapOf("lastKey" to mapOf("_id" to 1, "title" to "Hello"))
        val req = SearchRequest(
            query = MatchAllQuery(),
            sorts = listOf(DocSort("title", direction = Direction.Descending)),
            fields = listOf(Field("name"), Field("title")),
            params = params
        ).deepPage(true)
        val req2 = xform.transformRequest(req, emptyMap())
        val q2 = req2.query
        assertTrue(q2 is AndQuery)
        // assertEquals(1, q2.gt)
    }


}
