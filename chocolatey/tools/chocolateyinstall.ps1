$version = '5.2.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '29E97F62A68D43E7724CB2B29B718406DFE45ECD52B5DC25AC21ED69980242AC'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
