<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="Identity.xsl"/>
	<xsl:template match="ASSET">
		<xsl:element name="ASSET{count(preceding::ASSET)+1}">
<!--			<xsl:attribute name="id"><xsl:value-of select="count(preceding::ASSET)+1"/></xsl:attribute>-->
			<xsl:apply-templates select="@*|node()"/>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>