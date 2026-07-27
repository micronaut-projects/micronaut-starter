$version = '5.1.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'F5B2EF899FDCD9FF8A3E53F5BA558C5B6F0CDC3BD8A3E949E91F8E0B2343B6FC'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
