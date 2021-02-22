$version = '2.3.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '6A4751A3E8B7A7D34037D9F656FBFA6F7A6E6C430BB3F364063F300836ACBD80'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
