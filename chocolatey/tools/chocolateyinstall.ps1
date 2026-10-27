$version = '5.2.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '6BD1CFE36AF9A4CDED504A370F73617E1164F06B61BF6E2E310DD2F63BED40CD'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
