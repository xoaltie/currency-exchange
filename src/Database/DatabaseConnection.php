<?php

namespace App\Database;

use PDO;

final class DatabaseConnection
{
    public function __construct(public PDO $dbh) {}

    /**
     * @param string $query
     * @return array<int,array{
     *     id: int,
     *     code: string,
     *     full_name: string,
     *     sign: string,
     * }>
     */
    public function exec(string $query): array
    {
        $sql = $this->dbh->query($query);

        if (!$sql){
            throw new \PDOException(
                "PDO can't execute query: {$query}.\n"
            );
        }

        $sql->execute();

        return $sql->fetchAll(PDO::FETCH_ASSOC);
    }
}
