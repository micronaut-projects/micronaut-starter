$version = '4.9.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'E09139C371EA09C1EC026604F27EC985AFF1C02D78D7D57FE51C2F072680567E'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
