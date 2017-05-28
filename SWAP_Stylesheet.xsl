<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="Identity.xsl"/>
	<xsl:template match="//node()[@TYPE = 'EntList']">
		<xsl:element name="{local-name()}" >
			<xsl:attribute name="TYPE">EntList</xsl:attribute>
			<xsl:attribute name="SINGLE">
				<xsl:value-of select="@SINGLE" />
			</xsl:attribute>
			<xsl:for-each select="*">
				<xsl:element name="{local-name()}{position()}">
					<xsl:apply-templates select="@*|node()"/>
				</xsl:element>
			</xsl:for-each>
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>