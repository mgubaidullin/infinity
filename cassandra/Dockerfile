FROM cassandra

COPY init.cql /init.cql
COPY entrypoint-wrap.sh /entrypoint-wrap.sh
RUN chmod +x /entrypoint-wrap.sh
ENTRYPOINT ["/entrypoint-wrap.sh"]
CMD ["cassandra", "-f"]
