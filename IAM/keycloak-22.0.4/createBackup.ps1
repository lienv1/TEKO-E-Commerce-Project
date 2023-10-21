# Get the local IPv4 address
$localIP = (Get-NetIPAddress -AddressFamily IPv4 | Where-Object { $_.PrefixOrigin -eq 'Dhcp' }).IPAddress

if ($null -eq $localIP) {
    Write-Host "No DHCP IPv4 address found"
    exit 1
}

try {
    # Using double quotes for PowerShell variable expansion. Escape inner double quotes with backticks.
    $command = "/opt/keycloak/bin/kc.sh export --dir /opt/keycloak/data/exportedRealm --db-url=jdbc:mysql://`"$localIP`"/Keycloak --db-username root --db-password my-secret-pw --users same_file"
    docker exec -it keycloak-container /bin/sh -c $command

    # Copy data from container to the current location
    $currentDir = $(Get-Location)
    docker cp keycloak-container:/opt/keycloak/data/exportedRealm $currentDir/backup/
}
catch {
    Write-Host "An error occurred: $_"
    exit 1
}
