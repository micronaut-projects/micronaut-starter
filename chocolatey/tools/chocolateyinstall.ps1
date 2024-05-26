$version = '4.4.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'ED8266998087D58F0C8E9E2C924CB31624E72BB9E63A53A91A11C7EA61AE8D2A'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
