$version = '4.7.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '960F412699AA1F0177BCF360D445C8C5E7E3868658081175E806DE1A6D09559F'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
