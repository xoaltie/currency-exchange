<?php

namespace App\Database;

use PDO;

final class DatabaseConnection
{
    public function __construct(public PDO $dbh) {}

    /**
     * @param string $query
     * @return array<int, array>
     */
    public function exec(string $query): array
    {
        $sth = $this->dbh->query($query);

        if (!$sth) {
            throw new \PDOException(
                "PDO can't execute query: {$query}.\n",
            );
        }

        $sth->execute();

        return $sth->fetchAll(PDO::FETCH_ASSOC);
    }

    /**
     * @param string $query
     * @param array<string,mixed> $params
     * @return array<int, array>
     */
    public function prepareExec(string $query, array $params): array
    {
        $sth = $this->dbh->prepare($query);

        $result = $sth->execute($params);

        if (!$result) {
            throw new \PDOException(
                "PDO can't execute query: {$query}.\n",
            );
        }

        return $sth->fetchAll(PDO::FETCH_ASSOC);
    }
}
