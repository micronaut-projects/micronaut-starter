$version = '2.3.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '611D528B11E7289FDD458921EDB831B627A1CFB9884FA549AD0EF9BE67C94C72'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
